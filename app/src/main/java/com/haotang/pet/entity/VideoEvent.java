package com.haotang.pet.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-08-11 15:57
 */
public class VideoEvent {
    private boolean isVideo;
    private String videoPath;
    private Object cameraId;
    public VideoEvent(boolean isVideo, String videoPath,Object cameraId) {
        this.isVideo = isVideo;
        this.videoPath = videoPath;
        this.cameraId = cameraId;
    }

    public Object getCameraId() {
        return cameraId;
    }

    public void setCameraId(Object cameraId) {
        this.cameraId = cameraId;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
