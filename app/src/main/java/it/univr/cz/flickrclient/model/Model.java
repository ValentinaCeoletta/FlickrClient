package it.univr.cz.flickrclient.model;

import net.jcip.annotations.GuardedBy;

import java.util.LinkedList;

import it.univr.cz.flickrclient.MVC;


/**
 * Model class: manages the data, logic and rules of the application
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class Model {
    /**
     * Search list of FlickrPic
     */
    private final @GuardedBy("itself") LinkedList<FlickrPic> pictures = new LinkedList<>();

    /**
     * Search list of last user FlickrPic
     */
    private final @GuardedBy("itself") LinkedList<FlickrPic> userPictures = new LinkedList<>();

    /**
     * The MVC triple
     */
    private MVC mvc;

    /**
     * Registers the mvc triple to this class
     * @param mvc MVc triple
     */
    public void setMVC(MVC mvc) {
        this.mvc = mvc;
    }

    /**
     * Cleans the list of FlickrPic
     * @param action intent action
     */
    public void clearList(Action action) {
        switch(action){
            case ACTION_DL_INFO:
            case ACTION_DL_IMAGE:
            case ACTION_DL_RECENT_PHOTO:
            case ACTION_DL_POPULAR_PHOTO:
            case IMAGE_RESULT_FRAGMENT:
                synchronized (pictures) {
                    pictures.clear();
                }
                break;
            case ACTION_DL_USER_PHOTO:
            case USER_IMAGE_RESULT_FRAGMENT:
                synchronized (userPictures) {
                    userPictures.clear();
                }
                break;
        }
    }

    /**
     * Stores the photo at the specified index in the list of FlickrPic
     * @param pic FlickrPic to store
     * @param index where to store the photo
     * @param action intent action
     */
    public void storePicture(FlickrPic pic, int index, Action action) {
        switch(action){
            case ACTION_DL_INFO:
            case ACTION_DL_IMAGE:
            case ACTION_DL_RECENT_PHOTO:
            case ACTION_DL_POPULAR_PHOTO:
            case IMAGE_RESULT_FRAGMENT:
                synchronized (pictures) {
                    pictures.add(index, pic);
                }
                break;
            case ACTION_DL_USER_PHOTO:
            case USER_IMAGE_RESULT_FRAGMENT:
                synchronized (userPictures) {
                    userPictures.add(index, pic);
                }
                break;
        }
    }

    /**
     * Returns a copy of the list of FlickrPic
     * @param action intent action
     * @return array di FlickPic
     */
    public FlickrPic[] getPictures(Action action) {
        switch(action){
            case ACTION_DL_INFO:
            case ACTION_DL_IMAGE:
            case ACTION_DL_RECENT_PHOTO:
            case ACTION_DL_POPULAR_PHOTO:
            case IMAGE_RESULT_FRAGMENT:
                synchronized (pictures) {
                    return pictures.toArray(new FlickrPic[pictures.size()]);
                }
            case ACTION_DL_USER_PHOTO:
            case USER_IMAGE_RESULT_FRAGMENT:
                synchronized (userPictures) {
                    return userPictures.toArray(new FlickrPic[userPictures.size()]);
                }
        }
        return null;

    }

    /**
     * Returns the position of a FlickrPic given the photo id
     * @param id the photo id
     * @param action intent action
     * @return the position of the FlickrPic
     */
    public int getPicturePosition(String id, Action action) {
        switch(action){
            case ACTION_DL_INFO:
            case ACTION_DL_IMAGE:
            case ACTION_DL_RECENT_PHOTO:
            case ACTION_DL_POPULAR_PHOTO:
            case IMAGE_RESULT_FRAGMENT:
                synchronized (pictures) {
                    for (FlickrPic pic : pictures)
                        if (pic.getId().equalsIgnoreCase(id))
                            return pictures.indexOf(pic);
                }
            case ACTION_DL_USER_PHOTO:
            case USER_IMAGE_RESULT_FRAGMENT:
                synchronized (userPictures) {
                    for (FlickrPic pic : userPictures)
                        if (pic.getId().equalsIgnoreCase(id))
                            return userPictures.indexOf(pic);
                }
        }
        return -1;
    }

    /**
     * Returns True if the LinkedList is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return pictures.isEmpty();
    }
}
