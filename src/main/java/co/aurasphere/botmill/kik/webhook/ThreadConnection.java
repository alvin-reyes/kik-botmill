package co.aurasphere.botmill.kik.webhook;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.Files;

import com.google.gson.JsonParser;

import co.aurasphere.botmill.kik.model.KikApi;
import co.aurasphere.botmill.kik.model.Utils;

/**
 * The Class ThreadNewConnection.
 */
public class ThreadConnection extends Thread {

	/** The socket. */
	private Socket socket;

	/** The kik api. */
	private KikApi kikApi;

	/**
	 * Instantiates a new thread new connection.
	 *
	 * @param socket
	 *            the socket
	 * @param kikApi
	 *            the kik api
	 */
	public ThreadConnection(Socket socket, KikApi kikApi) {
		this.socket = socket;
		this.kikApi = kikApi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			System.out.println("New connection from " + socket.getInetAddress());
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = ".";
			StringBuilder builder = new StringBuilder();

			int contentLength = 0;
			String signature = "";

			StringBuilder postData = new StringBuilder();

			int x = 0;

			boolean isPost = true;
			String fileRequested = "";

			while (true) {
				x++;
				line = reader.readLine();
				builder.append(line).append("\n");

				if (line.startsWith("GET")) {
					fileRequested = line.substring(5).split(" ")[0];
				}

				if (line.startsWith("Content-Length")) {
					contentLength = Integer.parseInt(line.split(":")[1].trim());
				}

				if (line.startsWith("X-Kik-Signature")) {
					signature = line.split(":")[1].trim();
				}

				if (line.isEmpty()) {
					if (isPost) {
						for (int i = 0; i < contentLength; i++) {
							int c = reader.read();
							postData.append((char) c);
						}
					}

					break;
				}

				if (x >= 500) {
					throw new RuntimeException();
				}
			}

			PrintStream writer = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

			writer.print("HTTP/1.0 200 OK\r\n");
			if (fileRequested.isEmpty()) {
				writer.print("Content-Type: text/html\r\n");
			} else if (kikApi.getListenerThread().getFileMap().containsKey(fileRequested)) {
				writer.print("Content-Type: "
						+ Files.probeContentType(kikApi.getListenerThread().getFileMap().get(fileRequested).toPath())
						+ "\r\n");
			}
			writer.print("Server: Java? I dont really know.\r\n");
			writer.print("\r\n");

			if (fileRequested.isEmpty()) {
				writer.print("This is totally text/html.");
			} else {
				System.out.println("File requested: " + fileRequested);
				FileInputStream fileInputStream = new FileInputStream(
						kikApi.getListenerThread().getFileMap().get(fileRequested));

				byte[] a = new byte[4096];
				int n;
				while ((n = fileInputStream.read(a)) > 0)
					writer.write(a, 0, n);

				fileInputStream.close();
			}

			// writer.write("<form method='post' action='index.php'><input
			// type='text' name='test'> <input type='submit'
			// name='submit'></form>");

			writer.flush();

			reader.close();
			writer.close();
			socket.close();
			System.out.println("Closed connection.");

			System.out.println("Was post " + isPost);

			if (!isPost)
				return;

			if (signature.isEmpty()) {
				throw new RuntimeException("Was a post request but did not get a signature header from kik.");
			}

			String hashed = Utils.calculateRFC2104HMAC(postData.toString(), kikApi.getApikey()).toUpperCase();

			if (!signature.equals(hashed)) {
				throw new RuntimeException("The SHA1-HMAC did not match");
			}

			System.out.println("Verifed message, this message was from kik.");

			kikApi.handleWebhookResponse(new JsonParser().parse(postData.toString()).getAsJsonObject());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(this.getName() + " failed to handle a connection from " + socket.getInetAddress() + "!");
		}
	}
}
