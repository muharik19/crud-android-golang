package lib

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