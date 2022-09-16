package amacic.utils;

import amacic.data.Tag;

import java.util.List;

public class TagUtil {
    public static boolean isEmpty(List<Tag> tags) {
        if (tags == null) {
            return true;
        } else {
            for (Tag tag : tags) {
                if (tag == null || tag.getValue() == null || tag.getValue().trim().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
}
