package com.exapmle.sample;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * 「JavaMail」を使用したメール送信クラス。<br>
 */
public class JavaMailSender {

    /** メール送信するための情報を保持する。 */
    private Session session = null;

    /**
     * コンストラクタ。<br>
     */
    public JavaMailSender() {
        // 設定ファイルからの読込を実施する。
        Properties properties = new Properties();
        try (InputStream is = JavaMailSender.class.getClassLoader().getResourceAsStream("smtp.properties")) {
            properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.session = Session.getDefaultInstance(properties);
    }

    /**
     * メール送信するためのメッセージクラスを作成する。<br>
     * @return メッセージクラス
     */
    public MimeMessage createMimeMessage() {
        return new MimeMessage(this.session);
    }

    /**
     * メール送信を実施する。<br>
     * @param message メッセージクラス
     */
    public void transport(MimeMessage message) {
        // メール送信を実施する。
        try {
            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
