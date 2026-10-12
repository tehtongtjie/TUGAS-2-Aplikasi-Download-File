import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.io.File;

public class DownloadFileGUI extends JFrame {

    private final Color BIRU  = new Color(37, 99, 235);
    private final Color HIJAU = new Color(22, 163, 74);
    private final Color MERAH = new Color(220, 38, 38);
    private final Color ABU   = new Color(100, 116, 139);

    private JTextField txtUrl, txtFolder;
    private JButton btnPilihFolder, btnDownload;
    private JProgressBar progressBar;
    private JLabel lblStatus;

    // Membuat jendela: header, komponen input, progress bar, lalu menyusunnya di kartu putih
    public DownloadFileGUI() {
        setTitle("Aplikasi Download File");
        setSize(560, 410);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel judul = new JLabel("Aplikasi Download File");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 41, 59));
        header.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        header.add(judul);

        String folderDefault = System.getProperty("user.home") + File.separator + "Downloads";
        txtUrl    = buatField("");
        txtFolder = buatField(folderDefault);

        btnPilihFolder = buatTombol("Pilih Folder", Color.WHITE, Color.DARK_GRAY);
        btnDownload    = buatTombol("Download", BIRU, Color.WHITE);

        progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new BasicProgressBarUI());
        progressBar.setStringPainted(true);
        progressBar.setForeground(BIRU);
        progressBar.setBackground(new Color(226, 232, 240));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 24));

        lblStatus = new JLabel("Masukkan URL lalu klik Download.");
        lblStatus.setForeground(ABU);

        JPanel kartu = new JPanel(new GridBagLayout());
        kartu.setBackground(Color.WHITE);
        kartu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.weightx = 1; c.gridwidth = 2;
        c.insets = new Insets(4, 0, 4, 0);

        c.gridy = 0; kartu.add(buatLabel("URL File"), c);
        c.gridy = 1; kartu.add(txtUrl, c);
        c.gridy = 2; kartu.add(buatLabel("Simpan ke Folder"), c);

        c.gridy = 3; c.gridwidth = 1;
        kartu.add(txtFolder, c);
        c.gridx = 1; c.weightx = 0; c.insets = new Insets(4, 8, 4, 0);
        kartu.add(btnPilihFolder, c);

        c.gridx = 0; c.weightx = 1; c.gridwidth = 2;
        c.insets = new Insets(14, 0, 4, 0);
        c.gridy = 4; kartu.add(btnDownload, c);
        c.gridy = 5; kartu.add(progressBar, c);
        c.insets = new Insets(4, 0, 0, 0);
        c.gridy = 6; kartu.add(lblStatus, c);

        JPanel isi = new JPanel(new BorderLayout());
        isi.setBackground(new Color(241, 245, 249));
        isi.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        isi.add(kartu, BorderLayout.NORTH);

        add(header, BorderLayout.NORTH);
        add(isi, BorderLayout.CENTER);

        btnPilihFolder.addActionListener(e -> pilihFolder());
        btnDownload.addActionListener(e -> mulaiDownload());
    }

    // Membuat label judul kolom dengan gaya yang seragam
    private JLabel buatLabel(String teks) {
        JLabel label = new JLabel(teks);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(ABU);
        return label;
    }

    // Membuat kolom teks dengan garis tipis dan jarak di dalamnya
    private JTextField buatField(String isi) {
        JTextField field = new JTextField(isi);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return field;
    }

    // Membuat tombol berwarna (BasicButtonUI supaya warna tidak ditimpa tema Windows)
    private JButton buatTombol(String teks, Color latar, Color tulisan) {
        JButton tombol = new JButton(teks);
        tombol.setUI(new BasicButtonUI());
        tombol.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tombol.setBackground(latar);
        tombol.setForeground(tulisan);
        tombol.setFocusPainted(false);
        tombol.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tombol.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));
        return tombol;
    }

    // Mengubah teks dan warna label status
    private void setStatus(String teks, Color warna) {
        lblStatus.setText(teks);
        lblStatus.setForeground(warna);
    }

    // Membuka dialog untuk memilih folder tujuan
    private void pilihFolder() {
        JFileChooser chooser = new JFileChooser(txtFolder.getText());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtFolder.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    // Validasi input lalu menjalankan DownloadTask di thread terpisah
    private void mulaiDownload() {
        String url = txtUrl.getText().trim();
        File folder = new File(txtFolder.getText().trim());

        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "URL belum diisi!");
            return;
        }
        if (!folder.isDirectory()) {
            JOptionPane.showMessageDialog(this, "Folder tujuan tidak ditemukan!");
            return;
        }

        setSedangProses(true);
        progressBar.setValue(0);
        progressBar.setForeground(BIRU);
        setStatus("Menghubungkan ke server...", BIRU);

        new DownloadTask(url, folder, this).execute();
    }

    // Mengupdate progress bar (persen = -1 berarti ukuran file tidak diketahui)
    public void updateProgress(int persen, long terunduh, long total) {
        if (persen < 0) {
            progressBar.setIndeterminate(true);
            setStatus("Terunduh: " + (terunduh / 1024) + " KB", BIRU);
        } else {
            progressBar.setIndeterminate(false);
            progressBar.setValue(persen);
            setStatus("Terunduh: " + (terunduh / 1024) + " KB dari " + (total / 1024) + " KB", BIRU);
        }
    }

    // Menampilkan pop-up download selesai lalu mengecek apakah file ZIP
    public void downloadSelesai(File file) {
        progressBar.setIndeterminate(false);
        progressBar.setValue(100);
        progressBar.setForeground(HIJAU);
        setStatus("Download selesai: " + file.getName(), HIJAU);

        JOptionPane.showMessageDialog(this,
                "Download selesai!\nFile disimpan di:\n" + file.getAbsolutePath());

        if (ZipHelper.isZip(file)) {
            tanyaEkstrak(file);
        } else {
            setSedangProses(false);
        }
    }

    // Menampilkan pesan error saat download gagal
    public void downloadGagal(String pesan) {
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        setStatus("Download gagal", MERAH);
        JOptionPane.showMessageDialog(this, "Download gagal:\n" + pesan,
                "Error", JOptionPane.ERROR_MESSAGE);
        setSedangProses(false);
    }

    // Memberi pilihan kepada pengguna: Ekstrak atau Tidak
    private void tanyaEkstrak(File fileZip) {
        Object[] pilihan = {"Ekstrak", "Tidak"};
        int jawab = JOptionPane.showOptionDialog(this,
                "File ini adalah file ZIP. Ingin diekstrak?", "File ZIP Terdeteksi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, pilihan, pilihan[0]);

        if (jawab == 0) {
            ekstrak(fileZip);
        } else {
            setStatus("File ZIP tidak diekstrak.", ABU);
            setSedangProses(false);
        }
    }

    // Mengekstrak ZIP di background ke folder bernama sama (data.zip -> folder "data")
    private void ekstrak(File fileZip) {
        String nama = fileZip.getName();
        if (nama.toLowerCase().endsWith(".zip")) {
            nama = nama.substring(0, nama.length() - 4);
        } else {
            nama = nama + "_extract";
        }
        File folderTujuan = new File(fileZip.getParentFile(), nama);

        setStatus("Mengekstrak...", BIRU);
        progressBar.setIndeterminate(true);

        new SwingWorker<Integer, Void>() {
            protected Integer doInBackground() throws Exception {
                return ZipHelper.extract(fileZip, folderTujuan);
            }

            protected void done() {
                progressBar.setIndeterminate(false);
                try {
                    int jumlah = get();
                    setStatus("Ekstrak selesai: " + jumlah + " file", HIJAU);
                    JOptionPane.showMessageDialog(DownloadFileGUI.this,
                            jumlah + " file diekstrak ke:\n" + folderTujuan.getAbsolutePath());
                } catch (Exception e) {
                    setStatus("Ekstrak gagal", MERAH);
                    JOptionPane.showMessageDialog(DownloadFileGUI.this,
                            "Ekstrak gagal:\n" + e.getCause(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                setSedangProses(false);
            }
        }.execute();
    }

    // Mengunci tombol dan input selama proses supaya tidak diklik dua kali
    private void setSedangProses(boolean proses) {
        btnDownload.setEnabled(!proses);
        btnPilihFolder.setEnabled(!proses);
        txtUrl.setEnabled(!proses);
        btnDownload.setBackground(proses ? ABU : BIRU);
    }
}