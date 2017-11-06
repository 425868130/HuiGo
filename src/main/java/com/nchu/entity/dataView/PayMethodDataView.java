package com.nchu.entity.dataView;

import com.nchu.enumdef.PaymentMethod;

public class PayMethodDataView {
    private PaymentMethod paymentMethod;
    private String discription;

    public PayMethodDataView() {
    }

    public PayMethodDataView(PaymentMethod paymentMethod, String discription) {
        this.paymentMethod = paymentMethod;
        this.discription = discription;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public static PayMethodDataView[] getAll() {
        return new PayMethodDataView[]{
                new PayMethodDataView(PaymentMethod.ACCOUNT, PaymentMethod.ACCOUNT.getDiscription()),
                new PayMethodDataView(PaymentMethod.ALI_PAY, PaymentMethod.ALI_PAY.getDiscription()),
                new PayMethodDataView(PaymentMethod.BANK_CARD, PaymentMethod.BANK_CARD.getDiscription()),
                new PayMethodDataView(PaymentMethod.OTHER, PaymentMethod.OTHER.getDiscription())
        };
    }
}
