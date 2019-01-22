package PwdHash;

import org.mindrot.jbcrypt.BCrypt;

public class PwdHash {

    private int logRounds;

    public PwdHash(int logRounds)
    {
        this.logRounds = logRounds;
    }

    public String hash(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }

    public boolean verifyHash(String password, String hash)
    {
        return BCrypt.checkpw(password, hash);
    }

}
