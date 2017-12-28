package com.sampling.Beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by zzf on 17-10-11.
 * ("ID" TEXT,"NO" TEXT,"Content" TEXT,"T_pid" TEXT,"Type" TEXT,"Belong" TEXT,"B_Valid" TEXT,"Shortcut" TEXT,"T_BZ" TEXT,"I_BZ" TEXT)
 */
@Entity(nameInDb = "dictionary")
public class Dictionary {
    @Property(nameInDb = "ID")
    String ID;
    @Property(nameInDb = "NO")
    String NO;
    @Property(nameInDb = "Content")
    String Content;
    @Property(nameInDb = "T_pid")
    String t_pid;
    @Property(nameInDb = "Type")
    String Type;
    @Property(nameInDb = "Belong")
    String Belong;
    @Property(nameInDb = "B_Valid")
    String b_valid;
    @Property(nameInDb = "Shortcut")
    String Shortcut;
    @Property(nameInDb = "T_BZ")
    String T_BZ;
    @Property(nameInDb = "I_BZ")
    String I_BZ;
    @Generated(hash = 195577533)
    public Dictionary(String ID, String NO, String Content, String t_pid, String Type, String Belong, String b_valid, String Shortcut,
                      String T_BZ, String I_BZ) {
        this.ID = ID;
        this.NO = NO;
        this.Content = Content;
        this.t_pid = t_pid;
        this.Type = Type;
        this.Belong = Belong;
        this.b_valid = b_valid;
        this.Shortcut = Shortcut;
        this.T_BZ = T_BZ;
        this.I_BZ = I_BZ;
    }
    @Generated(hash = 487998537)
    public Dictionary() {
    }
    public String getID() {
        return this.ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getNO() {
        return this.NO;
    }
    public void setNO(String NO) {
        this.NO = NO;
    }
    public String getContent() {
        return this.Content;
    }
    public void setContent(String Content) {
        this.Content = Content;
    }
    public String getT_pid() {
        return this.t_pid;
    }
    public void setT_pid(String t_pid) {
        this.t_pid = t_pid;
    }
    public String getType() {
        return this.Type;
    }
    public void setType(String Type) {
        this.Type = Type;
    }
    public String getBelong() {
        return this.Belong;
    }
    public void setBelong(String Belong) {
        this.Belong = Belong;
    }
    public String getB_valid() {
        return this.b_valid;
    }
    public void setB_valid(String b_valid) {
        this.b_valid = b_valid;
    }
    public String getShortcut() {
        return this.Shortcut;
    }
    public void setShortcut(String Shortcut) {
        this.Shortcut = Shortcut;
    }
    public String getT_BZ() {
        return this.T_BZ;
    }
    public void setT_BZ(String T_BZ) {
        this.T_BZ = T_BZ;
    }
    public String getI_BZ() {
        return this.I_BZ;
    }
    public void setI_BZ(String I_BZ) {
        this.I_BZ = I_BZ;
    }
}
