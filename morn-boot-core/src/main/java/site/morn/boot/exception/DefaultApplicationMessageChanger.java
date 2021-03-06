package site.morn.boot.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import site.morn.bean.annotation.Target;
import site.morn.exception.ApplicationMessage;
import site.morn.exception.ApplicationMessages;
import site.morn.exception.ExceptionProperties;
import site.morn.translate.Transfer;
import site.morn.translate.TranslateChanger;
import site.morn.translate.Translator;
import site.morn.translate.Translators;

/**
 * 默认应用消息转换器
 *
 * @author timely-rain
 * @since 1.0.0, 2018/12/10
 */
@RequiredArgsConstructor
@Target(ApplicationMessage.class)
public class DefaultApplicationMessageChanger implements TranslateChanger<ApplicationMessage> {

  /**
   * 翻译器
   */
  private final Translator translator;

  /**
   * 异常配置项
   */
  private final ExceptionProperties properties;

  @Override
  public ApplicationMessage change(Transfer transfer) {
    String code = StringUtils.isEmpty(transfer.getCode()) ? properties.getDefaultCode()
        : transfer.getCode();
    // 格式化国际编码
    String messageCode = Translators
        .formatCode(properties.getPrefix(), code, properties.getMessageSuffix());
    String solutionCode = Translators
        .formatCode(properties.getPrefix(), code, properties.getSolutionSuffix());
    // 翻译
    String message = translator.translate(messageCode, transfer.getArgs());
    String solution = translator.translate(solutionCode, transfer.getArgs());
    // 构建应用消息
    return ApplicationMessages.buildMessage(code, message, solution);
  }
}
