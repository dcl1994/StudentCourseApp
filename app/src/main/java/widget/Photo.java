package widget;

/**
 * Created by szjdj on 2017-03-10.
 */
public class Photo {

    private String name;
    private int imageId;

    public Photo(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }
}
