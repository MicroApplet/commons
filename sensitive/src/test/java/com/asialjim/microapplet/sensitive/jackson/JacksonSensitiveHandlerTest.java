package com.asialjim.microapplet.sensitive.jackson;

import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import com.asialjim.microapplet.sensitive.SensitiveType;
import com.asialjim.microapplet.sensitive.annotation.Sensitive;
import com.asialjim.microapplet.sensitive.encrypt.*;
import com.asialjim.microapplet.sensitive.handler.*;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class JacksonSensitiveHandlerTest {
    @Before
    public void before() throws NoSuchAlgorithmException {
        new BankCardSensitiveHandler();
        new ChineseCitizenIdCardSensitiveHandler();
        new ChineseMobilePhoneSensitiveHandler();
        new ChineseNameSensitiveHandler();
        new ChineseTellPhoneSensitiveHandler();
        new CustomerSensitiveHandler();
        new EMailSensitiveHandler();
        new EnglishNameSensitiveHandler();

        SecretKey secretKey = KeyManager.generateModernEncryptionKey();

        AlgorithmModeConfig algorithmModeConfig = () -> AlgorithmMode.MODERN;
        SensitiveEncryptProperties properties = new SensitiveEncryptProperties();
        properties.setMode(algorithmModeConfig.getCurrentMode().getCode());
        EncryptionContext ctx = properties.encryptionContext();

        List<EncryptionContext> contexts = List.of(ctx);
        SecretKeyRepository secretKeyRepository = mode -> {
            SecretKeyRepository.Pair pair = new SecretKeyRepository.Pair();
            pair.setEncKey(secretKey);
            return pair;
        };

        EncryptionContextBean bean = new EncryptionContextBean(contexts, algorithmModeConfig, secretKeyRepository);
        bean.init();;
    }

    @Test
    public void test() {

        User user = new User();
        user.setName("王二");
        user.setPhone("15148784548");
        user.setMail("foolar@demo.com");
        String str = JsonUtil.instance.toStr(user);
        System.out.println(str);
        User bean = JsonUtil.instance.toBean(str, User.class);
        System.out.println(bean);

    }
}

@Data
class User implements Serializable {
    @Sensitive(SensitiveType.ChineseName)
    private String name;
    @Sensitive(SensitiveType.ChineseMobilePhone)
    private String phone;
    @Sensitive(SensitiveType.EMail)
    private String mail;
}
