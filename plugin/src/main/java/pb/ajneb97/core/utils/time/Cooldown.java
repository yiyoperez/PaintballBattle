package pb.ajneb97.core.utils.time;

public class Cooldown {

    private long start = System.currentTimeMillis();
    private long expire;
    private boolean notified;
    private long duration;

    public Cooldown(long duration) {
        this.duration = duration;
        this.expire = this.start + this.duration;
    }

    public boolean isNotified() {
        if (hasExpired()) {
            setNotified(true);
        }
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public long getExpire() {
        return expire;
    }

    public void retimeCooldown(long duration) {
        this.duration = duration;
        this.start = System.currentTimeMillis();
        this.expire = this.start + this.duration;
        setNotified(false);
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 0;
    }

    public String getTimeLeft() {
        if (this.getRemaining() >= 60_000) {
            return getTimeLeftRoundedSeconds();
        }
        return getTimeLeftSeconds();
    }

    public String getTimeLeftRoundedSeconds() {
        return TimeUtil.millisToRoundedTime(this.getRemaining());
    }

    public String getTimeLeftSeconds() {
        return TimeUtil.millisToSeconds(this.getRemaining());
    }

    public String getTimeLeftPlainSeconds() {
        return TimeUtil.millisToPlainSeconds(this.getRemaining());
    }

    public String getTimeLeftTimer() {
        return TimeUtil.millisToTimer(this.getRemaining());
    }
}