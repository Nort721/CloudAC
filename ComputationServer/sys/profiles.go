package sys

var profile_list = make(map[string]Profile)

type Profile struct {
	uuid            string
	isAllowedFlight bool
}

func Add(uuid string) {
	profile_list[uuid] = Profile{uuid, false}
}

func Remove(uuid string) {

}

func contains(uuid string) bool {
	return false
}

func get(uuid string) Profile {
	return profile_list[uuid]
}
