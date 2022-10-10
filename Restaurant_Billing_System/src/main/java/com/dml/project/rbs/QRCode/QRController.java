package com.dml.project.rbs.QRCode;

import com.google.zxing.WriterException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/RBS")
public class QRController {

    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/QRCode.png";
    @PreAuthorize("hasRole('Admin')")
    @GetMapping("/QRCode")
    public byte[] getQRCode(Model model){
        String path="rbs-project.netlify.app";


        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = QRCodeGenerator.getQRCodeImage(path,250,250);

            // Generate and Save Qr Code Image in static/image folder
            QRCodeGenerator.generateQRCodeImage(path,250,250,QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);

        model.addAttribute("image",path);
        model.addAttribute("file",path);
        model.addAttribute("qrcode",qrcode);

        return image;
    }
}