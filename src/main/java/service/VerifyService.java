package service;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;


// Xác minh chữ kí
public class VerifyService {
    public static boolean verifySignature(String data, String signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes("UTF-8"));
        return sig.verify(Base64.getDecoder().decode(signature));
    }
}
