package com.hwc.nfc;

/* Do it! 안드로이드 앱 프로그래밍 Part2 Chapter12 SampleNFC 코드 참조 */

public interface ParsedRecord {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_URI = 2;

    public int getType();

}