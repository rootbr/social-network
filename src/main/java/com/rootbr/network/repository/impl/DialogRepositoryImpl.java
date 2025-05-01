package com.rootbr.network.repository.impl;

import com.rootbr.network.model.DialogMessage;
import com.rootbr.network.repository.DialogRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.*;

@Repository
public class DialogRepositoryImpl implements DialogRepository {
  private final DSLContext dsl;

  @Autowired
  public DialogRepositoryImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  @Transactional
  public void sendMessage(DialogMessage message) {
    dsl.insertInto(table("dialog_messages"))
        .set(field("from_user_id"), message.getFrom())
        .set(field("to_user_id"), message.getTo())
        .set(field("text"), message.getText())
        .execute();
  }

  @Override
  public List<DialogMessage> getDialog(String userId1, String userId2) {
    Result<Record> result = dsl.select()
        .from(table("dialog_messages"))
        .where(
            (field("from_user_id").eq(userId1).and(field("to_user_id").eq(userId2)))
                .or(field("from_user_id").eq(userId2).and(field("to_user_id").eq(userId1)))
        )
        .orderBy(field("created_at"))
        .fetch();

    List<DialogMessage> messages = new ArrayList<>();
    for (Record record : result) {
      messages.add(mapRecordToDialogMessage(record));
    }

    return messages;
  }

  private DialogMessage mapRecordToDialogMessage(Record record) {
    DialogMessage message = new DialogMessage();
    message.setFrom(record.get("from_user_id", String.class));
    message.setTo(record.get("to_user_id", String.class));
    message.setText(record.get("text", String.class));
    return message;
  }
}
