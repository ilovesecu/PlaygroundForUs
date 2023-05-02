package ilovepc.playgroundforus.base.constant;

public enum BadgeColor {
    //pr:primakry, se:secondary, sc:success, dr:danger, wr:warning,in:info, lg:light,da:dark
    PR("primary"), SE("secondary"), SC("success"), DR("danger"), WR("warning"), IN("info"), LG("light"), DA("dark");
    private String color;

    BadgeColor(String color) {
        this.color = color;
    }

    String getColor(){
        return this.color;
    }
}
