package my;

import java.io.Serializable;

import com.pisight.pimoney.constants.Constants;
import com.pisight.pimoney.util.AccountUtil;

public class DataField  implements Serializable{

	private static final long serialVersionUID = -2964108779998215521L;

	private String name;

	private String value;

	private String type = "String";
	
	public DataField() {
		
	}

	public DataField(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public DataField(String name, String value, String type) throws Exception {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 * @throws Exception 
	 */
	public void setValue(String value) throws Exception {
		if (type == null) {
			this.value = value;
		}
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 * @throws Exception 
	 */
	public void setType(String type) throws Exception {
		if (value == null) {
			this.type = type;
		}
	}
	
	public String toString() {
		return "{name : " + this.name + ", value : " + this.value + ", type : " + this.type + "}";
	}

}
