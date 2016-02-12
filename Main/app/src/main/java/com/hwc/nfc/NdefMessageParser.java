package com.hwc.nfc;

/* Do it! 안드로이드 앱 프로그래밍 Part2 Chapter12 SampleNFC 코드 참조 */

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.util.ArrayList;
import java.util.List;

public class NdefMessageParser {

    // 생성자
    private NdefMessageParser() { }

    public static List<ParsedRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedRecord> getRecords(NdefRecord[] records) {

        List<ParsedRecord> elements = new ArrayList<ParsedRecord>();

        for (NdefRecord record : records) {

            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            }
            else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            }
        }
        return elements;
    }
}