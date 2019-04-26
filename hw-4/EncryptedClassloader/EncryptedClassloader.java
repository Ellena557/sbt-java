package ru.edhunter.Encrypted;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EncryptedClassloader extends ClassLoader {
    private final String key;
    private final File dir;
    private int encodingNumber = 57;

    public EncryptedClassloader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) {
        String className = name.replace('.', '/') + ".class";
        String path = dir.getPath() + "/" + className;

        byte[] bytes = loadEncryptedClass(name, path);

        byte[] decryptedBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            decryptedBytes[i] = decrypt(bytes[i]);
        }

        Class<?> clazz = defineClass(name, decryptedBytes, 0, decryptedBytes.length);
        return clazz;
    }

    private byte[] loadEncryptedClass(String name, String path) {
        byte[] encryption = null;
        try {
            encryption = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < encryption.length; i++) {
            encryption[i] = encrypt(encryption[i]);
        }

        return encryption;
    }

    private byte encrypt(byte b) {
        int num = b + key.hashCode() + encodingNumber;
        return (byte) num;
    }

    private byte decrypt(byte b) {
        int num = b - key.hashCode() - encodingNumber;
        return (byte) num;
    }
}
