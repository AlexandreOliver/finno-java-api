package api.financas.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public record Money(BigDecimal value) {
  public static Pattern format = Pattern.compile("^[0-9]+(?:[,.][0-9]{1,2})?//$");
  public static BigDecimal CEM =  new BigDecimal(100);

  public Money {
    if (value.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Money: O valor nao pode ser negativo");

    if (value.scale() > 2) throw new IllegalArgumentException("Money: O valor precisa ter apenas 2 casas decimais");
  }

  public static Money of(String value) {

    if (!value.matches(format.pattern())) {
      throw new IllegalArgumentException("Money: O valor informado esta mal formato: " + value);
    }

    return new Money(new BigDecimal(value.replace(',', '.')));
  }

  public static Money fromDb(Integer value) {

    if (value < 0) {
      throw new IllegalArgumentException("Money: O valor nao pode ser negativo");
    }

    BigDecimal cents = new BigDecimal(value);

    BigDecimal amount = cents.divide(CEM, 2, RoundingMode.HALF_EVEN);

    return new Money(amount);
  }

  public Integer inCents() {
    return value.multiply(CEM).intValue();
  }

  public Money sub(Money other) {

    if (value.compareTo(other.value()) < 0) {
      throw new IllegalArgumentException(String.format("Money: O Valor atual %f é menor que o valor a diminuir %f",  value, other.value()));
    }

    return new Money(value.subtract(other.value()));
  }

  public Money sum(Money other) {
    return new Money(value.add(other.value()));
  }
}
