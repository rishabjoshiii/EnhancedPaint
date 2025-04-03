import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UpiQrCodeGenerator {

    public static void main(String[] args) {
        String upiId = "example@bank"; 
        String payeeName = "Example Singh"; 
        String amount = "1000"; 
        String transactionNote = "Payment for services"; 
        generateUpiQrCode(upiId, payeeName, amount, transactionNote);
    }

    public static void generateUpiQrCode(String upiId, String payeeName, String amount, String transactionNote) {
        // Construct the UPI payment URL
        String upiUrl = "upi://pay?pa=" + upiId + "&pn=" + payeeName;
        if (amount != null && !amount.isEmpty()) {
            upiUrl += "&am=" + amount;
        }
        if (transactionNote != null && !transactionNote.isEmpty()) {
            upiUrl += "&tn=" + transactionNote;
        }

        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(upiUrl, BarcodeFormat.QR_CODE, 300, 300);
            BufferedImage qrImage = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }

            // Save the QR code as an image file
            File outputFile = new File("upi_qr_code.png");
            ImageIO.write(qrImage, "png", outputFile);
            System.out.println("QR Code generated: " + outputFile.getAbsolutePath());
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}