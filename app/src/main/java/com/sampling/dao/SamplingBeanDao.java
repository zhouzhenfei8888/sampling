package com.sampling.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.sampling.Beans.SamplingBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SAMPLING_BEAN".
*/
public class SamplingBeanDao extends AbstractDao<SamplingBean, Long> {

    public static final String TABLENAME = "SAMPLING_BEAN";

    /**
     * Properties of entity SamplingBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Isupload = new Property(1, String.class, "isupload", false, "ISUPLOAD");
        public final static Property Caiyanno = new Property(2, String.class, "caiyanno", false, "CAIYANNO");
        public final static Property Renwuno = new Property(3, String.class, "renwuno", false, "RENWUNO");
        public final static Property Yangpinglb = new Property(4, String.class, "yangpinglb", false, "YANGPINGLB");
        public final static Property Yangpingmc = new Property(5, String.class, "yangpingmc", false, "YANGPINGMC");
        public final static Property Yangpingmc2 = new Property(6, String.class, "yangpingmc2", false, "YANGPINGMC2");
        public final static Property Gps = new Property(7, String.class, "gps", false, "GPS");
        public final static Property Caiyangshuliang = new Property(8, String.class, "caiyangshuliang", false, "CAIYANGSHULIANG");
        public final static Property Time = new Property(9, String.class, "time", false, "TIME");
        public final static Property Strogemethond = new Property(10, String.class, "strogemethond", false, "STROGEMETHOND");
        public final static Property User = new Property(11, String.class, "user", false, "USER");
        public final static Property Pwd = new Property(12, String.class, "pwd", false, "PWD");
        public final static Property Candi = new Property(13, String.class, "candi", false, "CANDI");
        public final static Property Signpath = new Property(14, String.class, "signpath", false, "SIGNPATH");
        public final static Property Givesignpath = new Property(15, String.class, "givesignpath", false, "GIVESIGNPATH");
        public final static Property Images = new Property(16, String.class, "images", false, "IMAGES");
        public final static Property Cscm = new Property(17, String.class, "cscm", false, "CSCM");
        public final static Property Twh = new Property(18, String.class, "twh", false, "TWH");
        public final static Property Detail = new Property(19, String.class, "detail", false, "DETAIL");
    }


    public SamplingBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SamplingBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SAMPLING_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ISUPLOAD\" TEXT," + // 1: isupload
                "\"CAIYANNO\" TEXT," + // 2: caiyanno
                "\"RENWUNO\" TEXT," + // 3: renwuno
                "\"YANGPINGLB\" TEXT," + // 4: yangpinglb
                "\"YANGPINGMC\" TEXT," + // 5: yangpingmc
                "\"YANGPINGMC2\" TEXT," + // 6: yangpingmc2
                "\"GPS\" TEXT," + // 7: gps
                "\"CAIYANGSHULIANG\" TEXT," + // 8: caiyangshuliang
                "\"TIME\" TEXT," + // 9: time
                "\"STROGEMETHOND\" TEXT," + // 10: strogemethond
                "\"USER\" TEXT," + // 11: user
                "\"PWD\" TEXT," + // 12: pwd
                "\"CANDI\" TEXT," + // 13: candi
                "\"SIGNPATH\" TEXT," + // 14: signpath
                "\"GIVESIGNPATH\" TEXT," + // 15: givesignpath
                "\"IMAGES\" TEXT," + // 16: images
                "\"CSCM\" TEXT," + // 17: cscm
                "\"TWH\" TEXT," + // 18: twh
                "\"DETAIL\" TEXT);"); // 19: detail
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SAMPLING_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SamplingBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String isupload = entity.getIsupload();
        if (isupload != null) {
            stmt.bindString(2, isupload);
        }
 
        String caiyanno = entity.getCaiyanno();
        if (caiyanno != null) {
            stmt.bindString(3, caiyanno);
        }
 
        String renwuno = entity.getRenwuno();
        if (renwuno != null) {
            stmt.bindString(4, renwuno);
        }
 
        String yangpinglb = entity.getYangpinglb();
        if (yangpinglb != null) {
            stmt.bindString(5, yangpinglb);
        }
 
        String yangpingmc = entity.getYangpingmc();
        if (yangpingmc != null) {
            stmt.bindString(6, yangpingmc);
        }
 
        String yangpingmc2 = entity.getYangpingmc2();
        if (yangpingmc2 != null) {
            stmt.bindString(7, yangpingmc2);
        }
 
        String gps = entity.getGps();
        if (gps != null) {
            stmt.bindString(8, gps);
        }
 
        String caiyangshuliang = entity.getCaiyangshuliang();
        if (caiyangshuliang != null) {
            stmt.bindString(9, caiyangshuliang);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(10, time);
        }
 
        String strogemethond = entity.getStrogemethond();
        if (strogemethond != null) {
            stmt.bindString(11, strogemethond);
        }
 
        String user = entity.getUser();
        if (user != null) {
            stmt.bindString(12, user);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(13, pwd);
        }
 
        String candi = entity.getCandi();
        if (candi != null) {
            stmt.bindString(14, candi);
        }
 
        String signpath = entity.getSignpath();
        if (signpath != null) {
            stmt.bindString(15, signpath);
        }
 
        String givesignpath = entity.getGivesignpath();
        if (givesignpath != null) {
            stmt.bindString(16, givesignpath);
        }
 
        String images = entity.getImages();
        if (images != null) {
            stmt.bindString(17, images);
        }
 
        String cscm = entity.getCscm();
        if (cscm != null) {
            stmt.bindString(18, cscm);
        }
 
        String twh = entity.getTwh();
        if (twh != null) {
            stmt.bindString(19, twh);
        }
 
        String detail = entity.getDetail();
        if (detail != null) {
            stmt.bindString(20, detail);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SamplingBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String isupload = entity.getIsupload();
        if (isupload != null) {
            stmt.bindString(2, isupload);
        }
 
        String caiyanno = entity.getCaiyanno();
        if (caiyanno != null) {
            stmt.bindString(3, caiyanno);
        }
 
        String renwuno = entity.getRenwuno();
        if (renwuno != null) {
            stmt.bindString(4, renwuno);
        }
 
        String yangpinglb = entity.getYangpinglb();
        if (yangpinglb != null) {
            stmt.bindString(5, yangpinglb);
        }
 
        String yangpingmc = entity.getYangpingmc();
        if (yangpingmc != null) {
            stmt.bindString(6, yangpingmc);
        }
 
        String yangpingmc2 = entity.getYangpingmc2();
        if (yangpingmc2 != null) {
            stmt.bindString(7, yangpingmc2);
        }
 
        String gps = entity.getGps();
        if (gps != null) {
            stmt.bindString(8, gps);
        }
 
        String caiyangshuliang = entity.getCaiyangshuliang();
        if (caiyangshuliang != null) {
            stmt.bindString(9, caiyangshuliang);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(10, time);
        }
 
        String strogemethond = entity.getStrogemethond();
        if (strogemethond != null) {
            stmt.bindString(11, strogemethond);
        }
 
        String user = entity.getUser();
        if (user != null) {
            stmt.bindString(12, user);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(13, pwd);
        }
 
        String candi = entity.getCandi();
        if (candi != null) {
            stmt.bindString(14, candi);
        }
 
        String signpath = entity.getSignpath();
        if (signpath != null) {
            stmt.bindString(15, signpath);
        }
 
        String givesignpath = entity.getGivesignpath();
        if (givesignpath != null) {
            stmt.bindString(16, givesignpath);
        }
 
        String images = entity.getImages();
        if (images != null) {
            stmt.bindString(17, images);
        }
 
        String cscm = entity.getCscm();
        if (cscm != null) {
            stmt.bindString(18, cscm);
        }
 
        String twh = entity.getTwh();
        if (twh != null) {
            stmt.bindString(19, twh);
        }
 
        String detail = entity.getDetail();
        if (detail != null) {
            stmt.bindString(20, detail);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SamplingBean readEntity(Cursor cursor, int offset) {
        SamplingBean entity = new SamplingBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // isupload
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // caiyanno
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // renwuno
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // yangpinglb
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // yangpingmc
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // yangpingmc2
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // gps
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // caiyangshuliang
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // time
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // strogemethond
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // user
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // pwd
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // candi
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // signpath
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // givesignpath
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // images
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // cscm
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // twh
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19) // detail
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SamplingBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIsupload(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCaiyanno(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRenwuno(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setYangpinglb(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setYangpingmc(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setYangpingmc2(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setGps(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCaiyangshuliang(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTime(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setStrogemethond(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setUser(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setPwd(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setCandi(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setSignpath(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setGivesignpath(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setImages(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setCscm(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setTwh(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setDetail(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SamplingBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SamplingBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SamplingBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
