package com.revbingo.kss;

public class Modifier {

	public String name;
	public String description;
    public String markup;
	
	public Modifier(String name, String description) {
		this.name = name;
		this.description = description;
	}

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getMarkup()
    {
        return markup;
    }

    public void setMarkup(String markup)
    {
        this.markup = markup;
    }
	
	public String className() {
		return name.replaceAll("\\.", "").replaceAll(":", " pseudo-class-").trim();
    }	
}
