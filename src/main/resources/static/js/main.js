let mainJs = {
    block: function (toBlockedAccountsId, redirectUrl) {

        if (toBlockedAccountsId === NaN) {
            alert("차단할 계정의 ID가 잘못되었습니다.");
        }

        $.ajax({
            url: "/api/v1/accounts/" + toBlockedAccountsId + "/block",
            method: "post",
        }).done(function () {
            alert("차단되었습니다");
            if (redirectUrl == null) {
                window.location.href = "";
            } else {
                window.location.href = redirectUrl;
            }
        }).fail(function (e) {
            alert("차단하는데 실패했습니다.");
        });
    },
}