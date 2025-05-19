package hr.algebra.flashback.metric

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class PhotoMetric(
    meterRegistry: MeterRegistry
) {
    private val uploadCounter = meterRegistry.counter("photo.upload.count")
    private val photoUpdateCounter = meterRegistry.counter("photo.update.count")
    private val photoDeleteCounter = meterRegistry.counter("photo.upload.delete.count")
    private val deleteAllPhotoCounter = meterRegistry.counter("photo.upload.delete.all")
    private val transformationCounter = meterRegistry.counter("photo.upload.transformation")

    fun incrementUploadCounter() {
        uploadCounter.increment()
    }

    fun incrementPhotoUpdateCounter() {
        photoUpdateCounter.increment()
    }

    fun incrementDeleteCounter() {
        photoDeleteCounter.increment()
    }

    fun incrementDeleteAllCounter() {
        deleteAllPhotoCounter.increment()
    }

    fun incrementTransformationCounter() {
        transformationCounter.increment()
    }

}