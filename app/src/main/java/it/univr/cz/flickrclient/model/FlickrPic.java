package it.univr.cz.flickrclient.model;

import android.graphics.Bitmap;

import java.util.LinkedList;

/**
 * FlickrPic class: represents a photo in the Flickr website
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class FlickrPic {
    /**
     * URL of the high resolution photo
     */
    private final String url_image;

    /**
     * URL of the preview photo
     */
    private final String url_preview;

    /**
     * Photo title
     */
    private final String title;

    /**
     * Owner id
     */
    private final String owner_id;

    /**
     * Photo id
     */
    private final String id;

    /**
     * List of comments
     */
    private final LinkedList<String> comments = new LinkedList<>();

    /**
     * Preview photo
     */
    private Bitmap preview;

    /**
     * High resolution photo
     */
    private Bitmap image;

    /**
     * Class constructor
     * @param url_image the URL giving the location of the high resolution photo
     * @param url_preview the URL giving the location of the preview photo
     * @param preview Bitmap of preview photo
     * @param image Bitmap of high resolution photo
     * @param title the photo title
     * @param owner_id the owner id
     * @param id the photo id
     */
    public FlickrPic(String url_image, String url_preview, Bitmap preview, Bitmap image, String title, String owner_id, String id) {
        this.title = title;
        this.url_image = url_image;
        this.preview = preview;
        this.image = image;
        this.url_preview = url_preview;
        this.owner_id = owner_id;
        this.id = id;
    }

    /**
     * Returns the URL of the high resolution photo
     * @return the URL giving the location of the high resolution photo
     */
    public String getUrlImage() {
        return url_image;
    }

    /**
     * Returns the URL of the preview photo
     * @return the URL giving the location of the preview photo
     */
    public String getUrlPreview() {
        return url_preview;
    }

    /**
     * Returns the photo title
     * @return the photo title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the owner id
     * @return the owner id
     */
    public String getOwnerId() {
        return owner_id;
    }

    /**
     * Returns the Bitmap of the preview photo
     * @return Bitmap of the preview photo
     */
    public Bitmap getPreview() {
        return preview;
    }

    /**
     * Sets the Bitmap of the preview photo
     * @param image Bitmap of the preview photo
     */
    public void setPreview(Bitmap image) {
        this.preview = image;
    }

    /**
     * Returns the list of comments
     * @return string array of comments
     */
    public String[] getComments() {
        return comments.toArray(new String[comments.size()]);
    }

    /**
     * Returns the Bitmap of the high resolution photo
     * @return Bitmap of the high resolution photo
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Sets the Bitmap of the high resolution photo
     * @param image Bitmap of the high resolution photo
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Returns the photo id
     * @return the photo id
     */
    public String getId() {
        return id;
    }

    /**
     * Adds comments to the list
     * @param comms string Iterable of comments
     */
    public void addComments(Iterable<String> comms) {
        for (String comm : comms)
            comments.add(comm);
    }

    /**
     * Returns the concatenation of title, high resolution photo url and preview photo url
     * @return concatenation of title, high resolution photo url and preview photo url
     */
    @Override
    public String toString() {
        return title + "\n" + url_image + "\n" + url_preview;
    }

}
