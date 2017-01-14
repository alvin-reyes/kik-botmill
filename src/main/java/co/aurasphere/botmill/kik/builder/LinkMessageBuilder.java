/**
 * 
 * MIT License
 *
 * Copyright (c) 2017 BotMill.io
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package co.aurasphere.botmill.kik.builder;

import co.aurasphere.botmill.kik.intf.Buildable;
import co.aurasphere.botmill.kik.intf.Keyboardable;
import co.aurasphere.botmill.kik.model.Attribution;
import co.aurasphere.botmill.kik.model.BaseBuilder;
import co.aurasphere.botmill.kik.model.KeyValuePair;
import co.aurasphere.botmill.kik.model.MessageType;
import co.aurasphere.botmill.kik.outgoing.model.LinkMessage;

/**
 * The Class LinkMessageBuilder.
 */
public class LinkMessageBuilder extends BaseBuilder implements Keyboardable<LinkMessageBuilder>,Buildable<LinkMessage> {
	
	/** The link message. */
	private static LinkMessage linkMessage;
	
	/** The instance. */
	private static LinkMessageBuilder instance;
	
	/** The keyboard builder. */
	private static KeyboardBuilder<LinkMessageBuilder> keyboardBuilder;
	
	/**
	 * Gets the single instance of LinkMessageBuilder.
	 *
	 * @return single instance of LinkMessageBuilder
	 */
	public static LinkMessageBuilder getInstance() {
		if (instance == null) {
			instance = new LinkMessageBuilder();
		}
		linkMessage = new LinkMessage();
		linkMessage.setType(MessageType.LINK);
		return instance;
	}
	
	/**
	 * Instantiates a new link message builder.
	 */
	public LinkMessageBuilder() {
		
		linkMessage = new LinkMessage();
		linkMessage.setType(MessageType.LINK);
	}
	
	/**
	 * Sets the url.
	 *
	 * @param url the url
	 * @return the link message builder
	 */
	public LinkMessageBuilder setUrl(String url) {
		linkMessage.setUrl(url);
		return this;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the title
	 * @return the link message builder
	 */
	public LinkMessageBuilder setTitle(String title) {
		linkMessage.setTitle(title);
		return this;
	}
	
	/**
	 * Sets the text.
	 *
	 * @param text the text
	 * @return the link message builder
	 */
	public LinkMessageBuilder setText(String text) {
		linkMessage.setText(text);
		return this;
	}
	
	/**
	 * Sets the pic url.
	 *
	 * @param picUrl the pic url
	 * @return the link message builder
	 */
	public LinkMessageBuilder setPicUrl(String picUrl) {
		linkMessage.setPicUrl(picUrl);
		return this;
	}
	
	/**
	 * Sets the no forward.
	 *
	 * @param noForward the no forward
	 * @return the link message builder
	 */
	public LinkMessageBuilder setNoForward(boolean noForward) {
		linkMessage.setNoForward(noForward);
		return this;
	}
	
	/**
	 * Sets the kik js data.
	 *
	 * @param kikJsData the kik js data
	 * @return the link message builder
	 */
	public LinkMessageBuilder setKikJsData(KeyValuePair kikJsData) {
		linkMessage.setKikJsData(kikJsData);
		return this;
	}
	
	/**
	 * Sets the attribution.
	 *
	 * @param attribution the attribution
	 * @return the link message builder
	 */
	public LinkMessageBuilder setAttribution(Attribution attribution) {
		linkMessage.setAttribution(attribution);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see co.aurasphere.botmill.kik.intf.Keyboardable#addKeyboard()
	 */
	@Override
	public KeyboardBuilder<LinkMessageBuilder> addKeyboard() {
		keyboardBuilder = new KeyboardBuilder<LinkMessageBuilder>(this);
		return keyboardBuilder;
	}
	
	/* (non-Javadoc)
	 * @see co.aurasphere.botmill.kik.intf.Keyboardable#endKeyboard()
	 */
	@Override
	public LinkMessageBuilder endKeyboard() {
		return (LinkMessageBuilder)keyboardBuilder.getParentBuilder();
	}
	
	/* (non-Javadoc)
	 * @see co.aurasphere.botmill.kik.intf.Buildable#build()
	 */
	@Override
	public LinkMessage build() {
		if(keyboardBuilder != null) {
			linkMessage.setKeyboard(keyboardBuilder.buildKeyboard());
		}
		return linkMessage;
	}
}
