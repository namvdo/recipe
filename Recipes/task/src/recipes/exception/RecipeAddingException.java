package recipes.exception;

public class RecipeAddingException extends RuntimeException {
    private static final long serialVersionUID = -7034897190745766939L;

    public RecipeAddingException(String message) {
        super(message);
    }
}
