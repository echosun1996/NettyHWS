package org.nettyhws.nettyhws.tools;

import java.util.HashMap;
import java.util.Map;

import org.nettyhws.nettyhws.agreement.ShareMessage;
import org.nettyhws.nettyhws.def.ResConfig;

/**
 * @author thenk008,echosun
 */
public class ShareCon {
	public ShareMessage getShareMessage(String uri, String bodyMessage) {
		Map<Object, Object> map = new HashMap<>();
		String params;// get 参数
		if (uri.indexOf("?") > 0) {// 存在GET参数
			params = uri.split("\\?")[1];
			uri = uri.split("\\?")[0];
			String[] names = params.split("&");
			for (String name : names) {
				String[] nameAndValue = name.split("=");
				map.put(nameAndValue[0], nameAndValue[1]);
			}
		}
		// 将每一个/后面的第一个字母大写
		// String[] urs = uri.split("/");

		byte[] bc = uri.getBytes();
		int k = 0;
		for (int i = 0; i < bc.length; i++) {
			if (bc[i] == 47) {
				bc[i] = 46;
				k = i;
			}
		}
		byte br = bc[k + 1];
		if (br > 96) {
			bc[k + 1] = (byte) (br - 32);
		}

		uri = ResConfig.get().getControl() + new String(bc);// uriuri.replace("/", ".")
		// System.out.println("end uri==" + uri);
		ShareMessage share = new ShareMessage();
		share.setBody(bodyMessage);
		share.setUri(uri);
		share.setParams(map);
		return share;
	}
}
