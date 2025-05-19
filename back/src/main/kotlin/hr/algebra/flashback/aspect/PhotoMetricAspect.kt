package hr.algebra.flashback.aspect

import hr.algebra.flashback.metric.PhotoMetric
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TrackPhotoUpload()

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TrackPhotoUpdate()


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TrackPhotoDelete()

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TrackDeleteAllPhotos()

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TrackTransformation()

@Aspect
@Component
class PhotoMetricAspect(private val photoMetric: PhotoMetric){
    @AfterReturning("@annotation(hr.algebra.flashback.aspect.TrackPhotoUpload)")
    fun trackPhotoUpload() {
        photoMetric.incrementUploadCounter()
    }

    @AfterReturning("@annotation(hr.algebra.flashback.aspect.TrackDeleteAllPhotos)")
    fun trackDeleteAllPhotos() {
        photoMetric.incrementDeleteAllCounter()
    }

    @AfterReturning("@annotation(hr.algebra.flashback.aspect.TrackPhotoUpdate)")
    fun trackPhotoUpdate() {
        photoMetric.incrementPhotoUpdateCounter()
    }

    @AfterReturning("@annotation(hr.algebra.flashback.aspect.TrackPhotoDelete)")
    fun trackPhotoDelete() {
        photoMetric.incrementDeleteCounter()
    }

    @AfterReturning("@annotation(hr.algebra.flashback.aspect.TrackTransformation)")
    fun trackTransformation() {
        photoMetric.incrementTransformationCounter()
    }
}
