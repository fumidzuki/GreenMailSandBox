package com.exapmle.sample;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

public class JavaMailSenderTest {

    @Test
    public void testTransport() throws Exception {

        // Setup
        // http://www.icegreen.com/greenmail/#deploy_standalone
        // 起動時のパラメータにより起動するポートを変更できます。
        // 以下の設定の場合は、ポート番号として「3025」で起動します。
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        // Test
        JavaMailSender target = new JavaMailSender();

        MimeMessage message = target.createMimeMessage();
        message.setRecipient(RecipientType.TO, new InternetAddress("to@example.com"));
        message.setFrom(new InternetAddress("from@example.com"));
        message.setSubject("マルチバイトの件名です！！");
        message.setText("マルチバイトの本文です。");

        target.transport(message);

        // Assert
        MimeMessage[] messages = greenMail.getReceivedMessages();
        MimeMessage actual = messages[0];
        Assert.assertThat(actual.getRecipients(RecipientType.TO)[0].toString(), CoreMatchers.is("to@example.com"));
        Assert.assertThat(actual.getFrom()[0].toString(), CoreMatchers.is("from@example.com"));
        Assert.assertThat(actual.getSubject(), CoreMatchers.is("マルチバイトの件名です！！"));
        Assert.assertThat(actual.getContent().toString(), CoreMatchers.is("マルチバイトの本文です。"));

        greenMail.stop();

    }

}
