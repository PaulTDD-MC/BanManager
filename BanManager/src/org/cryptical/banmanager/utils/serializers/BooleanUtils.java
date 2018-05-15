package org.cryptical.banmanager.utils.serializers;

public class BooleanUtils {
	
	public static int toInt(boolean bool) {
		return bool ? 1 : 0;
	}

	public static boolean fromInt(int bool) {
		if (bool == 1) return true;
		return false;
	}
}
