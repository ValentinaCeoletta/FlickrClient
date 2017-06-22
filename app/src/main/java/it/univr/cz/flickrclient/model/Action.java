package it.univr.cz.flickrclient.model;

/**
 * Action enum: represents the actions perform in the application
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public enum Action {
    /**
     * The action of download photos information
     */
    ACTION_DL_INFO,

    /**
     * The action of download photos
     */
    ACTION_DL_IMAGE,

    /**
     * The action of download recent photos
     */
    ACTION_DL_RECENT_PHOTO,

    /**
     * The action of download popular photos
     */
    ACTION_DL_POPULAR_PHOTO,

    /**
     * The action of download last user photos
     */
    ACTION_DL_USER_PHOTO,

    /**
     * General action in image result fragment
     */
    IMAGE_RESULT_FRAGMENT,

    /**
     * General action in user image result fragment
     */
    USER_IMAGE_RESULT_FRAGMENT;
}


