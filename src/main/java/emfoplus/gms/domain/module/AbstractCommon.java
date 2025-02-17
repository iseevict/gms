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

    /**
     * 어떤 DB에 넣어야 할지 모르겠어서 껍데기만 만들어 둔 상태
     */
    @Override
    public void runChecker() {
        return;
    }
}
