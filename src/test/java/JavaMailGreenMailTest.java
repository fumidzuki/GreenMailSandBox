import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

public class JavaMailGreenMailTest {

  private GreenMail greenMail;

  @Before
  public void before() {
    // テスト用SMTPサーバを起動します、
    this.greenMail = new GreenMail(ServerSetupTest.SMTP);
    this.greenMail.start();
  }

  @After
  public void after() {
    // テスト用SMTPサーバを終了します。
    this.greenMail.stop();
  }

  @Test
  public void testTransport() throws Exception {

    // メール送信処理を実施します。
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "127.0.0.1");
    properties.put("mail.smtp.port", "3025");
    Session session = Session.getDefaultInstance(properties);

    // メール送信内容を設定します。
    MimeMessage message = new MimeMessage(session);
    message.setRecipient(RecipientType.TO, new InternetAddress("to@example.com"));
    message.setFrom(new InternetAddress("from@example.com"));
    message.setSubject("マルチバイトの件名です！！");
    message.setText("マルチバイトの本文です。");

    // メールを送信します。
    Transport.send(message);

    // メール送信結果を取得します。
    // 宛先、送信元、件名、本文などの確認をできます。
    MimeMessage[] messages = this.greenMail.getReceivedMessages();
    Assert.assertThat(messages.length, CoreMatchers.is(1));

    MimeMessage m = messages[0];
    Assert.assertThat(m.getRecipients(RecipientType.TO)[0],
        CoreMatchers.is(new InternetAddress("to@example.com")));
    Assert.assertThat(m.getFrom()[0], CoreMatchers.is(new InternetAddress("from@example.com")));
    Assert.assertThat(m.getSubject(), CoreMatchers.is("マルチバイトの件名です！！"));
    Assert.assertThat(m.getContent().toString(), CoreMatchers.is("マルチバイトの本文です。"));

  }

}
