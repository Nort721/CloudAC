package sys

var profile_map = make(map[string]Profile)

type SpeedCheckData struct {
	vl          int
	lastOffsetH float64
}

type Location struct {
	x     float64
	y     float64
	z     float64
	yaw   float32
	pitch float32
}

type Profile struct {
	uuid                  string
	isFlyingClient        bool
	isFlyingServer        bool
	isAllowedFlightClient bool
	isAllowedFlightServer bool
	onGround              bool
	lastOnGround          bool
	latestLocation        Location
	lastLocation          Location
	speedData             SpeedCheckData
}

func Add(uuid string) {
	profile_map[uuid] = Profile{uuid, false, false, false, false, true, true, Location{}, Location{}, SpeedCheckData{}}
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
