let index = {
    init:function (){
        let btnSave = document.getElementById("btn-save");
        btnSave.addEventListener("click", ()=>{
            this.save();
        });
    },

    save:function (){
        console.log("save 시작");
        let data = {
            username:document.getElementById("username").value,
            password:document.getElementById("password").value,
            email:document.getElementById("email").value,
            phone:document.getElementById("phone").value,
            address:document.getElementById("address").value
        };

        let regexEmail = new RegExp("([!#-'*+/-9=?A-Z^-~-]+(\.[!#-'*+/-9=?A-Z^-~-]+)*|\"\(\[\]!#-[^-~ \t]|(\\[\t -~]))+\")@([!#-'*+/-9=?A-Z^-~-]+(\.[!#-'*+/-9=?A-Z^-~-]+)*|\[[\t -Z^-~]*])");
        if(!regexEmail.test(data.email)){
            alert("이메일 형식이 올바르지 않습니다.");
        } else {
            $.ajax({
                type: "POST",
                url: "/auth/joinProc",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function (resp) {
                if (resp.status === 500) {
                    alert("회원가입 실패");
                } else {
                    alert("회원가입 완료");
                }
                location.href = "/";
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }

    }
}