package com.rootbr.network.application.command;

import static org.slf4j.LoggerFactory.getLogger;

import java.time.Instant;
import javax.sql.DataSource;
import org.slf4j.Logger;

public abstract class Command {
    protected static final Logger log = getLogger(Command.class);

    protected final Instant instant = Instant.now();
    protected String principalId;
    protected String principalLogin;

    public boolean authorize(final String id, final String login) {
        principalId = id;
        principalLogin = login;
        return false;
    }

    public abstract void execute(final DataSource dataSource);
}
