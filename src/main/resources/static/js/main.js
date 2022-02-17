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
            mainJs.alertErrorMessage(e, "차단하는데 실패했습니다.");
        });
    },

    alertErrorMessage: function (error, defaultMessage) {
        let statusCode = error['status'];
        if (statusCode == 401) {
            alert("다시 로그인 해주세요.");
            window.location.href = "/login";

            return;
        }

        if (error["responseJSON"] != "undefined") {
            alert(defaultMessage + "\n" + error["responseJSON"]["message"]);
            return;
        }

        if (defaultMessage == null) {
            alert("요청에 실패했습니다.");
            return;
        }

        alert(defaultMessage);
    }
}