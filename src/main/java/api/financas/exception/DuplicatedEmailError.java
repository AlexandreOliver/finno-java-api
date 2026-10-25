package api.financas.exception;

public class DuplicatedEmailError extends RuntimeException {
  public final String nameError = "DuplicatedEmailError";
  public DuplicatedEmailError(String message) {
    super(message);
  }
}
