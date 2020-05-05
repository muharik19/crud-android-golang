package main

import (
	"database/sql"
	"encoding/json"
	"net/http"
	"fmt"
	"log"
	"time"

	"github.com/gin-gonic/gin"
	_ "github.com/go-sql-driver/mysql"
)

var (
	dbport = "3306"
	dbaddr = "127.0.0.1"
)

type RespUpload struct {
	Status  int    `json:"status"`
	Message string `json:"message"`
}

type RespMahasiswa struct {
	ID      int    `json:"id"`
	NIM     string `json:"nim"`
	Nama    string `json:"nama"`
	No_Hp   string `json:"noHp"`
	Jurusan string `json:"jurusan"`
	Photo   string `json:"photo"`
}

type ListMahasiswaResultRes struct {
	Success     bool            `json:"success"`
	Message     string          `json:"message"`
	Messagecode string          `json:"messagecode"`
	Results     *ListResultsAll `json:"results"`
}

type ListResultsAll struct {
	ListMahasiswa []RespMahasiswa `json:"list_mahasiswa"`
}

func main() {
	gin.SetMode(gin.ReleaseMode)
	r := gin.New()
	v := r.Group("unpam/v1/")
	{
		v.POST("mahasiswaadd/", MahasiswaAdd)
		v.GET("mahasiswa/", Mahasiswa)
		v.POST("mahasiswaupdate/", MahasiswaUpdate)
		v.POST("mahasiswadelete/", DeleteMahasiswa)
		v.Static("/images", "./images")
	}
	r.Run(":83")
}

func MahasiswaAdd(c *gin.Context) {
	// var uploadOk int
	// uploadOk = 1;
	db, err := sql.Open("mysql", "root:@tcp("+dbaddr+":"+dbport+")/go_db")

	if err != nil {
		log.Println(err.Error())
	}
	defer db.Close()

	t := time.Now()
	datetime := t.Format("20060102150405")
	nim := c.PostForm("nim")
	nama := c.PostForm("nama")
	jurusan := c.PostForm("jurusan")
	no_hp := c.PostForm("no_hp")
	file, err := c.FormFile("image")

	if err != nil {
		c.String(http.StatusBadRequest, fmt.Sprintf("err: %s", err.Error()))
		return
	}

	imageName := "http://192.168.43.237:83/unpam/v1/images/" + datetime + file.Filename
	path := "images/" + datetime + file.Filename
	if err := c.SaveUploadedFile(file, path); err != nil {
		c.String(http.StatusBadRequest, fmt.Sprintf("err: %s", err.Error()))
		return
	}

	stmt, err := db.Prepare(`INSERT INTO tbl_mahasiswa SET nama=?, photo=?, nim=?, jurusan=?, no_hp=?`)
	if err == nil {
		_, err := stmt.Exec(&nama, &imageName, &nim, &jurusan, &no_hp)
		if err != nil {
			log.Println(err.Error())
			return
		}
	}
	defer stmt.Close()

	// if uploadOk == 0 {
	// 	RespVer := RespUpload {
	// 		Status:  0,
	// 		Message: "Image type is not supported.",
	// 	}
	// 	Respjson, _ := json.Marshal(RespVer)
	// 	c.String(200, string(Respjson))
	// } else {
		RespVer := RespUpload {
			Status:  1,
			Message: "Created new mahasiswa successfully.",
		}
		Respjson, _ := json.Marshal(RespVer)
		c.String(200, string(Respjson))
	// }
}

func Mahasiswa(c *gin.Context) {
	var nim, nama, noHp, jurusan, photo string
	var id int
	var ListMhsElementsStruct []RespMahasiswa

	db, err := sql.Open("mysql", "root:@tcp("+dbaddr+":"+dbport+")/go_db")
	if err != nil {
		log.Println(err.Error())
		c.JSON(500, gin.H{"Error ": err.Error()})
		return
	}
	defer db.Close()

	rows, err := db.Query(`SELECT id, nim, nama, no_hp, jurusan, photo FROM tbl_mahasiswa ORDER BY id DESC`)
	if err != nil {
		log.Println(err.Error())
		c.JSON(500, gin.H{"Error ": err.Error()})
		return
	}
	defer rows.Close()

	for rows.Next() {
		rows.Scan(&id, &nim, &nama, &noHp, &jurusan, &photo)
		ListMhsElementsStruct = append(ListMhsElementsStruct, RespMahasiswa {
			ID     : id,
			NIM    : nim,
			Nama   : nama,
			No_Hp  : noHp,
			Jurusan: jurusan,
			Photo  : photo,
		})
	}

	ListMhsRespStruct := ListMahasiswaResultRes {
		Success:     true,
		Message:     "Success",
		Messagecode: "00",
		Results: &ListResultsAll {
			ListMahasiswa: ListMhsElementsStruct,
		},
	}
	jsonRes, _ := json.Marshal(ListMhsRespStruct)
	c.String(200, string(jsonRes))
}

func MahasiswaUpdate(c *gin.Context) {
	db, err := sql.Open("mysql", "root:@tcp("+dbaddr+":"+dbport+")/go_db")

	if err != nil {
		log.Println(err.Error())
	}
	defer db.Close()
	t := time.Now()
	datetime := t.Format("20060102150405")
	id := c.PostForm("id")
	nim := c.PostForm("nim")
	nama := c.PostForm("nama")
	jurusan := c.PostForm("jurusan")
	no_hp := c.PostForm("no_hp")
	file, err := c.FormFile("image")

	if err != nil {
		c.String(http.StatusBadRequest, fmt.Sprintf("err: %s", err.Error()))
		return
	}

	imageName := "http://192.168.43.237:83/unpam/v1/images/" + datetime + file.Filename
	path := "images/" + datetime + file.Filename
	if err := c.SaveUploadedFile(file, path); err != nil {
		c.String(http.StatusBadRequest, fmt.Sprintf("err: %s", err.Error()))
		return
	}

	stmt, err := db.Prepare(`UPDATE tbl_mahasiswa SET nama=?, photo=?, nim=?, jurusan=?, no_hp=? WHERE id = ?`)
	if err == nil {
		_, err := stmt.Exec(&nama, &imageName, &nim, &jurusan, &no_hp, &id)
		if err != nil {
			log.Println(err.Error())
			return
		}
	}
	defer stmt.Close()

	RespVer := RespUpload {
		Status:  1,
		Message: "Updated mahasiswa successfully.",
	}
	Respjson, _ := json.Marshal(RespVer)
	c.String(200, string(Respjson))
}

func DeleteMahasiswa(c *gin.Context) {
	db, err := sql.Open("mysql", "root:@tcp("+dbaddr+":"+dbport+")/go_db")

	if err != nil {
		log.Println(err.Error())
	}
	defer db.Close()

	// id := c.Param("id")
	id := c.PostForm("id")

	if err != nil {
		c.String(http.StatusBadRequest, fmt.Sprintf("err: %s", err.Error()))
		return
	}

	stmt, err := db.Prepare(`DELETE FROM tbl_mahasiswa WHERE id = ?`)
	if err == nil {
		_, err := stmt.Exec(&id)
		if err != nil {
			log.Println(err.Error())
			return
		}
	}
	defer stmt.Close()

	RespVer := RespUpload {
		Status:  1,
		Message: "Delete mahasiswa successfully.",
	}
	Respjson, _ := json.Marshal(RespVer)
	c.String(200, string(Respjson))
}