package com.googlecode.ajui;

public class ALabel implements AInlineComponent {
	private final String text;

	public ALabel(String text) {
		this.text = text;
	}

	@Override
	public XHTMLTagger getHTML(Context context) {
		return new XHTMLTagger().text(text);
	}
}
