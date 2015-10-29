package py.edu.uca.intercajas.shared;

public class UnknownException extends Exception  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UnknownException(){
    }
    public UnknownException(String message) {
        super(message);
    }
}