package sys

var profile_map = make(map[string]Profile)

type Profile struct {
	uuid                  string
	isFlyingClient        bool
	isFlyingServer        bool
	isAllowedFlightClient bool
	isAllowedFlightServer bool
	x                     float64
	y                     float64
	z                     float64
	yaw                   float32
	pitch                 float32
}

func Add(uuid string) {
	profile_map[uuid] = Profile{uuid, false, false, false, false, 0, 0, 0, 0, 0}
}

func Remove(uuid string) {
	delete(profile_map, uuid)
}

func Contains(uuid string) bool {
	if _, keyExists := profile_map[uuid]; keyExists {
		return true
	}
	return false
}

func Get(uuid string) Profile {
	return profile_map[uuid]
}
