import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHelper {

    // 4 byte awal yang selalu dimiliki file ZIP: "PK" 03 04
    private static final int ZIP_MAGIC = 0x504B0304;

    // Mengecek apakah file ZIP dengan membaca 4 byte pertamanya (DataInputStream.readInt)
    public static boolean isZip(File file) {
        if (file.length() < 4) {
            return false;
        }
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            return in.readInt() == ZIP_MAGIC;
        } catch (IOException e) {
            return false;
        }
    }

    // Mengekstrak isi ZIP ke folderTujuan, mengembalikan jumlah file yang diekstrak
    public static int extract(File fileZip, File folderTujuan) throws IOException {
        folderTujuan.mkdirs();
        String pathTujuan = folderTujuan.getCanonicalPath() + File.separator;
        int jumlah = 0;

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip))) {
            ZipEntry entry;
            byte[] buffer = new byte[4096];

            while ((entry = zis.getNextEntry()) != null) {
                File fileBaru = new File(folderTujuan, entry.getName());

                if (!fileBaru.getCanonicalPath().startsWith(pathTujuan)) {
                    throw new IOException("Isi ZIP mencurigakan: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    fileBaru.mkdirs();
                } else {
                    fileBaru.getParentFile().mkdirs();
                    try (FileOutputStream fOut = new FileOutputStream(fileBaru)) {
                        int len;
                        while ((len = zis.read(buffer)) != -1) {
                            fOut.write(buffer, 0, len);
                        }
                    }
                    jumlah++;
                }
                zis.closeEntry();
            }
        }
        return jumlah;
    }
}