package main

import (
	"github.com/gin-gonic/gin"
	L "github.com/harik/api/lib"
)

func main() {
	gin.SetMode(gin.ReleaseMode)
	r := gin.New()
	v := r.Group("unpam/v1/")
	{
		v.POST("mahasiswaadd/", L.MahasiswaAdd)
		v.GET("mahasiswa/", L.Mahasiswa)
		v.POST("mahasiswaupdate/", L.MahasiswaUpdate)
		v.POST("mahasiswadelete/", L.DeleteMahasiswa)
		v.Static("/images", "./images")
	}
	r.Run(":83")
}