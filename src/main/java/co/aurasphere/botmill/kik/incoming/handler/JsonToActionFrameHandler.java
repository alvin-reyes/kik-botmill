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
package co.aurasphere.botmill.kik.incoming.handler;

import java.util.ArrayList;
import java.util.List;

import co.aurasphere.botmill.kik.builder.ActionFrameBuilder;
import co.aurasphere.botmill.kik.factory.EventFactory;
import co.aurasphere.botmill.kik.factory.ReplyFactory;
import co.aurasphere.botmill.kik.json.JsonUtils;
import co.aurasphere.botmill.kik.model.ActionFrame;
import co.aurasphere.botmill.kik.model.Event;
import co.aurasphere.botmill.kik.model.Frame;
import co.aurasphere.botmill.kik.model.JsonTextAction;
import co.aurasphere.botmill.kik.model.JsonToActionFrame;
import co.aurasphere.botmill.kik.network.NetworkUtils;
import co.aurasphere.botmill.kik.outgoing.reply.TextMessageReply;

/**
 * The Class JsonToActionFrameHandler.
 * 
 * @author Alvin P. Reyes
 */
public class JsonToActionFrameHandler {

	/**
	 * Json to text message reply.
	 *
	 * @param jsonUrl
	 *            the json url
	 * @return the list
	 */
	public static List<Frame> jsonToFrameReply(String jsonUrl) {

		List<Frame> list = new ArrayList<Frame>();
		String json = NetworkUtils.get(jsonUrl);
		JsonToActionFrame a = JsonUtils.fromJson(json, JsonToActionFrame.class);

		for (JsonTextAction jaction : a.getJsonTextAction()) {
			if (jaction.getType().equals("text")) {
				list.add(ActionFrameBuilder.getInstance().setEvent(EventFactory.textMessage(jaction.getInput()))
						.addReply(ReplyFactory.buildTextMessageReply(jaction.getOutput())).build());
			}
			if (jaction.getType().equals("pattern")) {
				list.add(ActionFrameBuilder.getInstance().setEvent(EventFactory.textMessagePattern(jaction.getInput()))
						.addReply(ReplyFactory.buildTextMessageReply(jaction.getOutput())).build());
			}
		}

		return list;
	}
}
