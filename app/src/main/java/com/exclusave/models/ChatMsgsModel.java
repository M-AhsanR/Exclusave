package com.exclusave.models;

public class ChatMsgsModel {
    String ChatMessageID;
    String CompressedMessageImage;
    String CreatedAt;
    String IsReadByReceiver;
    String IsReadBySender;
    String Message;
    String MessageImage;
    String ReceiverID;
    String ReceiverImage;
    String ReceiverName;
    String ReceiverUserID;
    String SenderID;
    String SenderImage;
    String SenderName;
    String SenderUserID;

    public ChatMsgsModel(String chatMessageID, String compressedMessageImage, String createdAt, String isReadByReceiver, String isReadBySender, String message, String messageImage, String receiverID, String receiverImage, String receiverName, String receiverUserID, String senderID, String senderImage, String senderName, String senderUserID) {
        ChatMessageID = chatMessageID;
        CompressedMessageImage = compressedMessageImage;
        CreatedAt = createdAt;
        IsReadByReceiver = isReadByReceiver;
        IsReadBySender = isReadBySender;
        Message = message;
        MessageImage = messageImage;
        ReceiverID = receiverID;
        ReceiverImage = receiverImage;
        ReceiverName = receiverName;
        ReceiverUserID = receiverUserID;
        SenderID = senderID;
        SenderImage = senderImage;
        SenderName = senderName;
        SenderUserID = senderUserID;
    }

    public String getChatMessageID() {
        return ChatMessageID;
    }

    public void setChatMessageID(String chatMessageID) {
        ChatMessageID = chatMessageID;
    }

    public String getCompressedMessageImage() {
        return CompressedMessageImage;
    }

    public void setCompressedMessageImage(String compressedMessageImage) {
        CompressedMessageImage = compressedMessageImage;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getIsReadByReceiver() {
        return IsReadByReceiver;
    }

    public void setIsReadByReceiver(String isReadByReceiver) {
        IsReadByReceiver = isReadByReceiver;
    }

    public String getIsReadBySender() {
        return IsReadBySender;
    }

    public void setIsReadBySender(String isReadBySender) {
        IsReadBySender = isReadBySender;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMessageImage() {
        return MessageImage;
    }

    public void setMessageImage(String messageImage) {
        MessageImage = messageImage;
    }

    public String getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(String receiverID) {
        ReceiverID = receiverID;
    }

    public String getReceiverImage() {
        return ReceiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        ReceiverImage = receiverImage;
    }

    public String getReceiverName() {
        return ReceiverName;
    }

    public void setReceiverName(String receiverName) {
        ReceiverName = receiverName;
    }

    public String getReceiverUserID() {
        return ReceiverUserID;
    }

    public void setReceiverUserID(String receiverUserID) {
        ReceiverUserID = receiverUserID;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    public String getSenderImage() {
        return SenderImage;
    }

    public void setSenderImage(String senderImage) {
        SenderImage = senderImage;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderUserID() {
        return SenderUserID;
    }

    public void setSenderUserID(String senderUserID) {
        SenderUserID = senderUserID;
    }
}
