package com.bitsworking.videoshowcase.datatypes;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * Created by Chris Hager <chris@linuxuser.at> on 07/09/14.
 */
public class ShowcaseItem {
    public enum SHOWCASE_ITEM_TYPE {
        VIDEO_LOCAL,
//        VIDEO_REMOTE,
//        IMAGE,
        LINK,
        INTERNAL
    }

    public SHOWCASE_ITEM_TYPE type = SHOWCASE_ITEM_TYPE.VIDEO_LOCAL;
    public String resourceFn = null;
    public String thumbnailFn = null;
    public String directory = null;
    public String text = "";
    public Bitmap bitmap_thumbnail = null;

    public ShowcaseItem(SHOWCASE_ITEM_TYPE type, String directory, String resourceFn, String thumbnailFn, String text) {
        this.type = type;
        this.directory = directory;
        this.resourceFn = resourceFn;
        this.thumbnailFn = thumbnailFn;

        if (this.thumbnailFn != null) {
            this.bitmap_thumbnail = BitmapFactory.decodeFile(getThumbnailFullFilename());
        }

        if (text != null) {
            this.text = text;

        } else if (type == SHOWCASE_ITEM_TYPE.VIDEO_LOCAL) {
            // If video, get text from filename
            this.text = resourceFn;
            this.text = this.text.substring(this.text.indexOf("_") + 1);
            this.text = this.text.substring(0, this.text.lastIndexOf("."));
            this.text = this.text.replaceAll("[-_]", " ");
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + String.format("[%s, %s, %s, '%s']", this.type, this.resourceFn, this.thumbnailFn, this.text);
    }

    /* Returns an Uri like Uri.parse("file:///storage/emulated/0/videos/video1.mp4") */
    public Uri getResourceUri() {
        if (type == SHOWCASE_ITEM_TYPE.VIDEO_LOCAL) {
            return Uri.parse("file://" + directory + "/" + resourceFn);
        } else {
            return Uri.parse(resourceFn);
        }
    }

    public String getResourceString() {
        return resourceFn;
    }

    /* Returns a full filename like `/storage/emulated/0/videos/video1.mp4` */
    public String getThumbnailFullFilename() {
        return (thumbnailFn == null) ? null : directory + "/" + thumbnailFn;
    }
}
