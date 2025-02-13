package emfoplus.gms.domain.module;

// Common에 대한 추상 클래스
public class AbstractCommon implements Common{
    protected boolean trigger = false;

    /**
     * trigger의 값을 true로 변경
     */
    @Override
    public void setTrigger() {
        this.trigger = true;
    }
}
