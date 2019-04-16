import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import com.octo.captcha.Captcha;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import javax.imageio.ImageIO;

public class generateJcaptcha {
    private static final Integer MIN_WORD_LENGTH = 4;// 验证码最小长度
    private static final Integer MAX_WORD_LENGTH = 4;// 验证码最大长度
    private static final Integer IMAGE_HEIGHT = 40;// 验证码图片高度
    private static final Integer IMAGE_WIDTH = 80;// 验证码图片宽度
    private static final Integer MIN_FONT_SIZE = 15;// 验证码最小字体
    private static final Integer MAX_FONT_SIZE = 15;// 验证码最大字体
    private static final String RANDOM_WORD = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";// 随机字符 // 验证码随机字体
    private static final Font[] RANDOM_FONT = new Font[]{new Font("nyala", Font.BOLD, MIN_FONT_SIZE), new Font("Arial", Font.BOLD, MIN_FONT_SIZE), new Font("Bell MT", Font.BOLD, MIN_FONT_SIZE), new Font("Credit valley", Font.BOLD, MIN_FONT_SIZE), new Font("Impact", Font.BOLD, MIN_FONT_SIZE)}; // 验证码随机颜色
    private static final Color[] RANDOM_COLOR = new Color[]{new Color(000, 000, 000)};
    private static ListImageCaptchaEngine captchaEngine;

    public static CaptchaEngine getCaptchaEngine(final String imgPath) {
        if (captchaEngine == null) {
            synchronized (generateJcaptcha.class) {
                if (captchaEngine == null && imgPath != null) {
                    captchaEngine = new ListImageCaptchaEngine() {
                        @Override
                        protected void buildInitialFactories() {
                            RandomListColorGenerator randomListColorGenerator = new RandomListColorGenerator(RANDOM_COLOR);
                            BackgroundGenerator backgroundGenerator = new FileReaderRandomBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT, imgPath);
                            WordGenerator wordGenerator = new RandomWordGenerator(RANDOM_WORD);
                            System.out.println(wordGenerator.getWord(4));
                            FontGenerator fontGenerator = new RandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE, RANDOM_FONT);
                            TextDecorator[] textDecorator = new TextDecorator[]{};
                            TextPaster textPaster = new DecoratedRandomTextPaster(MIN_WORD_LENGTH, MAX_WORD_LENGTH, randomListColorGenerator, textDecorator);
                            WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
                            addFactory(new GimpyFactory(wordGenerator, wordToImage));

                        }
                    };
                }
            }
        }
        return captchaEngine;
    }
    public static void main(String[] args){
        CaptchaEngine ce=generateJcaptcha.getCaptchaEngine("C:\\Users\\Administrator\\Desktop\\百度信用验证码\\验证码空白背景");
        Captcha captcha=ce.getNextCaptcha();
        System.out.println(captcha.getQuestion());
        BufferedImage image=(BufferedImage) captcha.getChallenge();
        try {
            ImageIO.write(image, "jpg", new File("C:\\Users\\Administrator\\Desktop\\百度信用验证码\\yuantu.jpg"));
        }catch (Exception e){
            System.out.println("写入错误");
        }

    }

}
