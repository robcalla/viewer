package it.eng.iot.servlet.model;

/*
 * 
 * "links": {
	"self": "http://cityenabler.eng.it:5000/v3/projects/cef31912da3a40668cda0cf89b6fc0a4/users/gambone/roles",
	"previous": null,
	"next": null
	},
 */
public class Link {
		
	private String self;
	private String previous;
	private String next;
	
	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}


	
	public Link(String self, String previous, String next) {
		this.self = self;
		this.previous = previous;
		this.next = next;
	}

	public Link(){
		this.self = "";
		this.previous = "";
		this.next = "";
	}


	
	
}
