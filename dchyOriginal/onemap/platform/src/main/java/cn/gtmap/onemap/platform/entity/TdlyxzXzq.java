package cn.gtmap.onemap.platform.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "OMP_TDLYXZ_XZQ")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TdlyxzXzq {

    @Id
    @Column(length = 12)
    private String DWDM;

    @Column(length = 255)
    private String DWMC;

    public String getDWDM() {
        return DWDM;
    }

    public void setDWDM(String DWDM) {
        this.DWDM = DWDM;
    }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }
}
