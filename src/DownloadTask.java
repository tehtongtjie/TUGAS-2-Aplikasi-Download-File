import javax.swing.SwingWorker;
import java.io.*;
import java.net.*;
import java.util.List;

// Proses download yang berjalan di thread terpisah supaya GUI tidak macet
public class DownloadTask extends SwingWorker<File, Long> {

    private final String urlStr;
    private final File folder;
    private final DownloadFileGUI gui;
    private long ukuranTotal = -1;

    public DownloadTask(String urlStr, File folder, DownloadFileGUI gui) {
        this.urlStr = urlStr;
        this.folder = folder;
        this.gui = gui;
    }

    // Berjalan di background: membuka koneksi, membaca data dari server, menyimpan ke file
    @Override
    protected File doInBackground() throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/124.0 Safari/537.36");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("Server membalas: " + status + " " + conn.getResponseMessage());
        }

        ukuranTotal = conn.getContentLengthLong();
        File fileTujuan = new File(folder, ambilNamaFile(conn.getURL()));

        try (InputStream in = conn.getInputStream();
             FileOutputStream fOut = new FileOutputStream(fileTujuan)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalTerbaca = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                fOut.write(buffer, 0, bytesRead);
                totalTerbaca += bytesRead;
                publish(totalTerbaca);
            }
        } finally {
            conn.disconnect();
        }
        return fileTujuan;
    }

    // Berjalan di thread GUI: menerima data dari publish() lalu mengupdate progress bar
    @Override
    protected void process(List<Long> data) {
        long terunduh = data.get(data.size() - 1);

        if (ukuranTotal > 0) {
            int persen = (int) (terunduh * 100 / ukuranTotal);
            gui.updateProgress(persen, terunduh, ukuranTotal);
        } else {
            gui.updateProgress(-1, terunduh, -1);
        }
    }

    // Berjalan di thread GUI setelah download selesai: memberi tahu GUI berhasil atau gagal
    @Override
    protected void done() {
        try {
            gui.downloadSelesai(get());
        } catch (Exception e) {
            Throwable penyebab = (e.getCause() != null) ? e.getCause() : e;
            String pesan;
            if (penyebab instanceof MalformedURLException) {
                pesan = "Format URL tidak valid. Cek kembali URL.";
            } else if (penyebab instanceof UnknownHostException) {
                pesan = "Server tidak ditemukan / tidak ada koneksi internet.";
            } else {
                pesan = penyebab.toString();
            }
            gui.downloadGagal(pesan);
        }
    }

    // Mengambil nama file dari URL, contoh: https://contoh.com/data.zip -> data.zip
    private String ambilNamaFile(URL url) {
        String path = url.getPath();
        String nama = path.substring(path.lastIndexOf('/') + 1);
        try {
            nama = URLDecoder.decode(nama, "UTF-8");
        } catch (Exception e) {
            // pakai nama apa adanya
        }
        if (nama.isEmpty()) {
            nama = "download_" + System.currentTimeMillis();
        }
        return nama;
    }
}